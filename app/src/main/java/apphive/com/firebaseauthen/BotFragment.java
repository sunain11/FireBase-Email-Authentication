package apphive.com.firebaseauthen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;

import static apphive.com.firebaseauthen.R.anim;
import static apphive.com.firebaseauthen.R.drawable;
import static apphive.com.firebaseauthen.R.id;
import static apphive.com.firebaseauthen.R.layout;

/**
 * Created by Sunain on 1/2/2018.
 */
public class BotFragment extends Fragment implements AIListener {
    RecyclerView recyclerView;
    private List<ChatMessage> chatList = new ArrayList<>();
    Recycleradapternew rcadapter;
    EditText editText;
    RelativeLayout addBtn;
    // DatabaseReference ref;
    // FirebaseRecyclerAdapter<ChatMessage, chat_rec> adapter;
    Boolean flagFab = true;
    ImageView fab_img;
    private AIService aiService;
    public BotFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(layout.fragment_bot, container, false);
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO},1);


        recyclerView = (RecyclerView)rootView.findViewById(id.recyclerView);
        editText = (EditText)rootView.findViewById(id.editText);
        addBtn = (RelativeLayout)rootView.findViewById(id.addBtn);
        fab_img = (ImageView)rootView.findViewById(id.fab_img);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        rcadapter=new Recycleradapternew(chatList);

        //ref = FirebaseDatabase.getInstance().getReference();
        //ref.keepSynced(true);

        final AIConfiguration config = new AIConfiguration("47eff963f6844232a2276e29fdd98b2c",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        aiService = AIService.getService(getActivity(), config);
        aiService.setListener(this);

        final AIDataService aiDataService = new AIDataService(config);

        final AIRequest aiRequest = new AIRequest();



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = editText.getText().toString().trim();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    recyclerView.setBackgroundResource(drawable.emoji2);
//                }
                if (!message.equals("")) {

                    ChatMessage chatMessage = new ChatMessage(message, "user");
                    //ref.child("chat").push().setValue(chatMessage);
                    chatList.add(chatMessage);
                    rcadapter.notifyDataSetChanged();

                    recyclerView.smoothScrollToPosition(rcadapter.getItemCount() - 1);
                    aiRequest.setQuery(message);
                    new AsyncTask<AIRequest,Void,AIResponse>(){

                        @Override
                        protected AIResponse doInBackground(AIRequest... aiRequests) {
                            final AIRequest request = aiRequests[0];
                            try {
                                final AIResponse response = aiDataService.request(aiRequest);
                                return response;
                            } catch (AIServiceException e) {
                            }
                            return null;
                        }
                        @Override
                        protected void onPostExecute(AIResponse response) {
                            if (response != null) {

                                Result result = response.getResult();
                                String reply = result.getFulfillment().getSpeech();
                                ChatMessage chatMessage = new ChatMessage(reply, "bot");
                                //ref.child("chat").push().setValue(chatMessage);
                                chatList.add(chatMessage);
                                rcadapter.notifyDataSetChanged();
                                recyclerView.smoothScrollToPosition(rcadapter.getItemCount() - 1);
                            }
                        }
                    }.execute(aiRequest);
                }
                else {
                    aiService.startListening();
                }

                editText.setText("");

            }
        });



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Bitmap img = BitmapFactory.decodeResource(getResources(), drawable.ic_send_white_24dp);
                Bitmap img1 = BitmapFactory.decodeResource(getResources(), drawable.ic_mic_white_24dp);


                if (s.toString().trim().length()!=0 && flagFab){
                    ImageViewAnimatedChange(getActivity(),fab_img,img);
                    flagFab=false;

                }
                else if (s.toString().trim().length()==0){
                    ImageViewAnimatedChange(getActivity(),fab_img,img1);
                    flagFab=true;

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        recyclerView.setAdapter(rcadapter);
        return rootView;
    }
    public void ImageViewAnimatedChange(Context c, final ImageView v, final Bitmap new_image) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, anim.zoom_out);
        final Animation anim_in  = AnimationUtils.loadAnimation(c, anim.zoom_in);
        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageBitmap(new_image);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }
    @Override
    public void onResult(AIResponse res) {
        Result result = res.getResult();

        String message = result.getResolvedQuery();
        ChatMessage chatMessage0 = new ChatMessage(message, "user");
        chatList.add(chatMessage0);

        recyclerView.smoothScrollToPosition(rcadapter.getItemCount() - 1);

        String reply = result.getFulfillment().getSpeech();
        ChatMessage chatMessage = new ChatMessage(reply, "bot");
        chatList.add(chatMessage);
        rcadapter.notifyDataSetChanged();

        recyclerView.smoothScrollToPosition(rcadapter.getItemCount() - 1);
    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
