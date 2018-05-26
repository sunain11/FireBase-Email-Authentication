package apphive.com.firebaseauthen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.logging.Handler;

/**
 * Created by Sunain on 1/3/2018.
 */

public class SplashFragment extends Fragment {
    ImageView spl;
    Animation animationFadeIn,animationFadeOut;
    int[] imgs={R.drawable.emoji,R.drawable.emoji2,R.drawable.emoji3,R.drawable.emoji4};
    int i;
    RelativeLayout rel;
    Handler handler;
    public SplashFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        spl=(ImageView)rootView.findViewById(R.id.splashimg);
        rel=(RelativeLayout)rootView.findViewById(R.id.relview1);
        animationFadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        animationFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
        i=1;
        Animation.AnimationListener animListener = new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(i>3){i=0;}

                    spl.setImageResource(imgs[i]);
                    spl.startAnimation(animationFadeIn);
                    i++;

            }
        };
        Animation.AnimationListener animListener1 = new Animation.AnimationListener(){

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                spl.startAnimation(animationFadeOut);
            }
        };
        spl.startAnimation(animationFadeOut);
        animationFadeIn.setAnimationListener(animListener1);
        animationFadeOut.setAnimationListener(animListener);
//        final float[] from = new float[3],
//                to =   new float[3];
//
//        Color.colorToHSV(Color.parseColor("#FFFFFFFF"), from);   // from white
//        Color.colorToHSV(Color.BLUE, to);     // to red
//
//        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);   // animate from 0 to 1
//        anim.setDuration(200);                              // for 300 ms
//
//        final float[] hsv  = new float[3];                  // transition color
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
//            @Override public void onAnimationUpdate(ValueAnimator animation) {
//                // Transition along each axis of HSV (hue, saturation, value)
//                hsv[0] = from[0] + (to[0] - from[0])*animation.getAnimatedFraction();
//                hsv[1] = from[1] + (to[1] - from[1])*animation.getAnimatedFraction();
//                hsv[2] = from[2] + (to[2] - from[2])*animation.getAnimatedFraction();
//
//                rel.setBackgroundColor(Color.HSVToColor(hsv));
//            }
//        });
//
//        anim.start();




//        ValueAnimator anim = ValueAnimator.ofInt(Color.WHITE, Color.BLUE);
//        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                rel.setBackgroundColor((Integer) animation.getAnimatedValue());
//            }
//        });
//        anim.setDuration(200);
//        anim.start();
        return rootView;
    }
}
