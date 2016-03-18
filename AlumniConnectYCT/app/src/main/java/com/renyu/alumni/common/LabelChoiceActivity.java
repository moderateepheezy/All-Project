package com.renyu.alumni.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.eyeem.chips.AutocompletePopover;
import com.eyeem.chips.BubbleSpan;
import com.eyeem.chips.ChipsEditText;
import com.eyeem.chips.Utils;
import com.renyu.alumni.R;

import java.util.ArrayList;
import java.util.HashMap;

public class LabelChoiceActivity extends Activity {
	
	ChipsEditText label_choice_edit=null;
	AutocompletePopover label_choice_popover=null;
	TextView label_choice_cancel=null;
	TextView label_choice_ok=null;
	
	ArrayList<String> tags=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_labelchoice);
		
		tags=new ArrayList<String>();
		
		init();
	}
	
	private void init() {
		label_choice_cancel=(TextView) findViewById(R.id.label_choice_cancel);
		label_choice_ok=(TextView) findViewById(R.id.label_choice_ok);
		label_choice_ok.setOnClickListener(new TextView.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!label_choice_edit.getText().toString().equals("")) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);    
					imm.hideSoftInputFromWindow(label_choice_edit.getWindowToken(), 0); 
					setEdit();
					Intent intent=getIntent();
					Bundle bundle=new Bundle();
					bundle.putStringArrayList("tags", tags);
					intent.putExtras(bundle);
					setResult(RESULT_OK, intent);
					finish();
				}
			}});
		label_choice_edit=(ChipsEditText) findViewById(R.id.label_choice_edit);
		label_choice_popover=(AutocompletePopover) findViewById(R.id.label_choice_popover);
		label_choice_edit.setMaxBubbleCount(10);
		label_choice_edit.setAutocomplePopover(label_choice_popover);
		label_choice_edit.setLineSpacing(1.0f, 1.5f);
	    label_choice_popover.setChipsEditText(label_choice_edit);
	    final ArrayList<String> availableItems=new ArrayList<String>();
	    label_choice_edit.setAutocompleteResolver(new ChipsEditText.AutocompleteResolver() {
			
			@Override
			public ArrayList<String> getSuggestions(String query) throws Exception {
				// TODO Auto-generated method stub
				return new ArrayList<String>();
			}
			
			@Override
			public ArrayList<String> getDefaultSuggestions() {
				// TODO Auto-generated method stub
				return availableItems;
			}
		});
	    label_choice_edit.addListener(new ChipsEditText.Listener() {
			
			@Override
			public void onXPressed() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onHashTyped(boolean start) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBubbleSelected(int position) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBubbleCountChanged() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onActionDone() {
				// TODO Auto-generated method stub
				setEdit();
			}
		});
	    tags=getIntent().getExtras().getStringArrayList("tags");
	    for(int i=0;i<tags.size();i++) {
	    	label_choice_popover.add(tags.get(i));
	    }
	}

	/**
	 * ��ȡ
	 * @return
	 */
	private String update() {
		// first flatten the text
		tags.clear();
		HashMap<Class<?>, Utils.FlatteningFactory> factories = new HashMap<Class<?>, Utils.FlatteningFactory>();
		factories.put(BubbleSpan.class, new AlbumFlatten());
		String flattenedText = Utils.flatten(label_choice_edit, factories);
		return flattenedText;
	}
	
	/**
	 * ���´�����ǩ
	 */
	private void setEdit() {
		String text=update();
		//���δ���
		if(text.trim().lastIndexOf("]")==-1) {
			label_choice_edit.setText("");
			label_choice_popover.add(text.trim());
			tags.add(text.trim());
		}
		//��������
		else if(text.trim().lastIndexOf("]")!=-1&&text.trim().lastIndexOf("]")!=text.trim().length()-1) {
			String last=text.trim().substring(text.trim().lastIndexOf("]")+1);
			label_choice_edit.setText("");
			for(int i=0;i<tags.size();i++) {
				label_choice_popover.add(tags.get(i).trim());
			}
			label_choice_popover.add(last.trim());
			tags.add(last.trim());
		}
	}
	
	private class AlbumFlatten implements Utils.FlatteningFactory {
		@Override
		public String out(String in) {
			if (in != null && in.startsWith("#")) {
				in = in.substring(1, in.length());
			}
			tags.add(in.trim());
			return "[a:"+in+"]";
		}
	}
}
