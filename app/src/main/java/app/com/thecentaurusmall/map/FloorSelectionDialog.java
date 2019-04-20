package app.com.thecentaurusmall.map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import app.com.thecentaurusmall.MainActivity;

public class FloorSelectionDialog extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface FloorSelectDialogListener {
        public void onDialogFloorClick(int which);
    }


    // Use this instance of the interface to deliver action events
    FloorSelectDialogListener listener;

    public void setDialogFloorClickListener(Context context) {
        this.listener = (FloorSelectDialogListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        CharSequence[] floors = new CharSequence[]{
                "Auto Floor (Enabled By Default)",
                "Fourth Floor",
                "Third Floor",
                "Second Floor",
                "First Floor",
                "Ground Floor"};

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Floor")
                .setItems(floors, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        listener.onDialogFloorClick(which);
                    }
                });
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface

        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (FloorSelectDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement FloorSelectionDialogListener");
        }
    }
}
