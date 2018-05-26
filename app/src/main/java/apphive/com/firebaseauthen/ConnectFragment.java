package apphive.com.firebaseauthen;

/**
 * Created by Sunain on 1/2/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ConnectFragment extends Fragment {

    TextView tv1,tv2;
    Button share;

    public ConnectFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_connect, container, false);
        tv1=(TextView)rootView.findViewById(R.id.label);
        tv2=(TextView)rootView.findViewById(R.id.textView2);
        share=(Button)rootView.findViewById(R.id.sharebutton);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharing();
            }
        });
        return rootView;
    }
    public void sharing()
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);

        StringBuilder sb = new StringBuilder();
        sb.append("Hi, I am using the Scrum Planning Poker. I like this and I want you to check it out.");
        sb.append("https://play.google.com/store/apps/details?id=" + this.getContext().getPackageName());
        sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void sharingdif()
    {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        StringBuilder sb = new StringBuilder();
        sb.append("Hi, I am using the Scrum Planning Poker. I like this and I want you to check it out.");
        sb.append("https://play.google.com/store/apps/details?id=" + this.getContext().getPackageName());
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
        startActivity(Intent.createChooser(sharingIntent, "Test"));
    }
//    public void sharefire()
//    {
//        Intent intent = new AppInviteInvitation().IntentBuilder(getString(R.string.invitation_title))
//                .setMessage(getString(R.string.invitation_message))
//                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
//                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
//                .setCallToActionText(getString(R.string.invitation_cta))
//                .build();
//        startActivityForResult(intent, REQUEST_INVITE);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
//
//        if (requestCode == REQUEST_INVITE) {
//            if (resultCode == RESULT_OK) {
//                // Get the invitation IDs of all sent messages
//                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
//                for (String id : ids) {
//                    Log.d(TAG, "onActivityResult: sent invitation " + id);
//                }
//            } else {
//                // Sending failed or it was canceled, show failure message to the user
//                // ...
//            }
//        }
//    }
}