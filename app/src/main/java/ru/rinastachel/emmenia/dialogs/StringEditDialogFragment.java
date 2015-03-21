package ru.rinastachel.emmenia.dialogs;

/*
public class StringEditDialogFragment extends DialogFragment {

	private String _title;
	private String _value;
	
	public static StringEditDialogFragment newInstance (Bundle args) {
		StringEditDialogFragment frag = new StringEditDialogFragment();
        frag.setArguments(args);
        return frag;
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Bundle args = getArguments();
    	parseArguments(args);
        
        Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(_title);
        
        EditText view = createEditText();
		view.setText(getValue());
		view.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setValue(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		dialog.setView(view);
		dialog.setPositiveButton(R.string.btn_ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_OK);
			}
		});
		
		dialog.setNegativeButton(R.string.btn_cancel, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sendResult(Activity.RESULT_CANCELED);
			}
		});
        
        return dialog.create();
    }
    
    protected String getValue() {
		return _value;
	}
    
	protected void setValue(String value) {
		if (value.length() > 0) {
			_value = value;
		} else {
			_value = null;
		}
	}

	protected EditText createEditText() {
		return new EditText (getActivity());
	}

	protected void parseArguments(Bundle args) {
		if (args.containsKey(DialogParam.TITLE_RESID)) {
			int resid = args.getInt(DialogParam.TITLE_RESID);
			_title = OptimumApplication.app().getResources().getString(resid);
		} else if (args.containsKey(DialogParam.TITLE)) {
			_title = args.getString(DialogParam.TITLE);
		}
		_value = args.getString(DialogParam.STRING_VALUE);
	}
	
	protected void sendResult (int resultCode) {
		if (getTargetFragment() == null) {
			return;
		}
		Intent intent = new Intent();
		Bundle args = getArguments();
		
		args.remove(DialogParam.STRING_VALUE);
		if (_value != null) {
			args.putString(DialogParam.STRING_VALUE, _value);
		}
		intent.putExtra(BaseActivity.KEY_BUNDLE, args);
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
	}
}
*/