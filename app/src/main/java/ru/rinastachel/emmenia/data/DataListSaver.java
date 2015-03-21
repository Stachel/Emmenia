package ru.rinastachel.emmenia.data;

import android.content.Context;
import android.os.Environment;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;

import ru.rinastachel.emmenia.exception.BackupDataException;
import ru.rinastachel.emmenia.exception.RestoreDataException;

public class DataListSaver {
	private Context _context;

	private static final String FILENAME = "emmenia.data";
	
	private static final String BACKUP_PATH = "Emmenia";
	private static final String BACKUP_FILENAME = "emmenia-";
	private static final String BACKUP_EXTENSION = ".data";
	
	public DataListSaver (Context ctx) {
		_context = ctx;
	}
	
	public void saveData(ArrayList<Entity> list) throws IOException {
		
		FileOutputStream fos = _context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		if (list != null && list.size() > 0) {
			for(Entity entity : list) {
				oos.writeObject(entity.getDate().getCalendar());
				oos.writeObject(entity.getComment());
			}
		}
		fos.flush();
		fos.close();
	}
	
	public ArrayList<Entity> readData() throws IOException, ClassNotFoundException  {
		FileInputStream fis = _context.openFileInput(FILENAME);
		return readDataFromStream (fis);
	}
	
	public ArrayList<Entity> readDataFromFile(String filePath) throws IOException, ClassNotFoundException  {
	    FileInputStream fis = new FileInputStream(new File(filePath));
	    return readDataFromStream (fis);
	}
	
	private ArrayList<Entity> readDataFromStream(FileInputStream fis) throws IOException, ClassNotFoundException {
		ArrayList<Entity> list = new ArrayList<Entity>();
		ObjectInputStream ois = new ObjectInputStream(fis);
		try {
			while (ois != null) {
				Calendar calendar = (Calendar)ois.readObject();
				String comment = (String)ois.readObject();
				list.add(new Entity(calendar, comment));
			}
		} catch (EOFException e) {
			
		}
		finally {
			if (fis != null) {
				fis.close();
			}
		}
		return list;
	}

	public boolean deleteFile() {
		return _context.deleteFile(FILENAME);
	}

	public String saveToSDCard() throws BackupDataException, FileNotFoundException {
		File dst = createBackupFilename();
		try {
			FileInputStream inStream = _context.openFileInput(FILENAME);
			FileOutputStream outStream = new FileOutputStream(dst);
			copy(inStream, outStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new BackupDataException();
		}
		return dst.getAbsolutePath();
	}
	
	public ArrayList<Entity> restoreFromSDCard(String path) throws RestoreDataException {
		try {
			return readDataFromFile(path);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RestoreDataException();
		}
	}

	private File createBackupFilename() {
		File path = new File(Environment.getExternalStorageDirectory(), BACKUP_PATH);
		if (!path.exists()) {
            path.mkdir();
        }
		String timestamp = (new Date()).getDashString();
		String filename = BACKUP_FILENAME + timestamp + BACKUP_EXTENSION;
		return new File (path, filename);
	}
	
	public void copy(FileInputStream inStream, FileOutputStream outStream) throws IOException {
	    FileChannel inChannel = inStream.getChannel();
	    FileChannel outChannel = outStream.getChannel();
	    inChannel.transferTo(0, inChannel.size(), outChannel);
	    inStream.close();
	    outStream.close();
	}
}
