package brijesh.com.pixy;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brijeshbharadwaj on 11/02/16.
 */
public class InboxFragment extends ListFragment{

    protected List<ParseObject> mMessages;
    public static final String TAG = InboxFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setProgressBarIndeterminateVisibility(true);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.orderByDescending(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {

                if (e == null) {
                    //works

                    mMessages = messages;

                    String[] senderNames = new String[mMessages.size()];
                    int i = 0;
                    for (ParseObject message : mMessages) {
                        senderNames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;
                    }

                    if (getListView().getAdapter() == null) {

                        MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);
                        setListAdapter(adapter);
                        getActivity().setProgressBarIndeterminateVisibility(false);
                    }

                    else{
                        ((MessageAdapter)getListView().getAdapter()).refill(mMessages);
                    }


                } else {
                    getActivity().setProgressBarIndeterminateVisibility(false);
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setMessage(e.getMessage())
                            .setTitle("Oops")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }



            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessages.get(position);
        String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        Uri fileUri = Uri.parse(file.getUrl());

        if (messageType.equals(ParseConstants.TYPE_IMAGE)){

            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.setData(fileUri);
            startActivity(intent);

        }

        else{
            Intent intent = new Intent(Intent.ACTION_VIEW,fileUri );
            intent.setDataAndType(fileUri, "video/*");
            startActivity(intent);

        }

        //delete the message!

        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);

        if (ids.size() == 1){
            //current user is the last recipient, delete message
            message.deleteInBackground();
        }

        else{

            ArrayList<String> idsToRemove = new ArrayList<String>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());
            message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
            message.saveInBackground();

        }


    }























}
