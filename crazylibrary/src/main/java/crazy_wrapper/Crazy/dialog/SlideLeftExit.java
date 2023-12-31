package crazy_wrapper.Crazy.dialog;

import android.util.DisplayMetrics;
import android.view.View;
import android.animation.ObjectAnimator;

public class SlideLeftExit extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "translationX", 0, -250 * dm.density), //
				ObjectAnimator.ofFloat(view, "alpha", 1, 0));
	}
}
