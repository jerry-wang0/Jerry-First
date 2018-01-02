package jerry.test.com.mvp.support;

import android.os.Bundle;

import cn.nextop.erebor.mid.R;

public final class Animations {
	// Type of animation
	public static final String BUNDLE_KEY_ANIM_EFFECT 			= "effectAnimationType";
	public static final int 	BUNDLE_KEY_ANIM_EFFECT_NO 		= 0;
	public static final int 	BUNDLE_KEY_ANIM_EFFECT_R2L 		= 1;
	public static final int 	BUNDLE_KEY_ANIM_EFFECT_L2R 		= 2;
	
	// Key to get Animation Id(s) for the tab
	public static final String BUNDLE_KEY_ANIM_ID_ENTER 		= "enterAnimationId";
	public static final String BUNDLE_KEY_ANIM_ID_EXIT 		= "exitAnimationId";
	public static final String BUNDLE_KEY_ANIM_ID_POP_ENTER 	= "popEnterAnimationId";
	public static final String BUNDLE_KEY_ANIM_ID_POP_EXIT 	= "popExitAnimationId";

	/**
	 * Remove all configurations used for screen transition.
	 * This method will hide the below entries from the bundle:
	 *		TabInfo.BUNDLE_KEY_ENTER_ANIM_ID
	 *		TabInfo.BUNDLE_KEY_EXIT_ANIM_ID
	 *		TabInfo.BUNDLE_KEY_POP_ENTER_ANIM_ID
	 *		TabInfo.BUNDLE_KEY_POP_EXIT_ANIM_ID
	 *
	 * @param bundle
	 */
	public static void clearScreenTransitionConfig(Bundle bundle) {
		if (bundle != null) {
			bundle.remove(BUNDLE_KEY_ANIM_ID_ENTER);
			bundle.remove(BUNDLE_KEY_ANIM_ID_EXIT);
			bundle.remove(BUNDLE_KEY_ANIM_ID_POP_ENTER);
			bundle.remove(BUNDLE_KEY_ANIM_ID_POP_EXIT);
		}
	}

	/**
	 * Add configuration for screen transition sliding in from left to right. This is used when
	 * need to restore a screen that is previously slided out (totally or partially).
	 * 
	 * @param bundle The bundle to add config to.
	 * @param nextScreenIsOutTotally If true, the quote screen will be placed outside totally.
	 *        Otherwise only a part of the quote screen will be put outside (say 30% of its width).
	 */
	public static void addScreenTransitionSlideInL2R(Bundle bundle, boolean nextScreenIsOutTotally) {
		if (bundle != null) {
			if (nextScreenIsOutTotally) {
				bundle.putInt(BUNDLE_KEY_ANIM_ID_ENTER, R.anim.slide_in_l2r);
				bundle.putInt(BUNDLE_KEY_ANIM_ID_EXIT, R.anim.slide_out_l2r);
			} else {
				bundle.putInt(BUNDLE_KEY_ANIM_ID_ENTER, R.anim.slide_in_l2r_30p);
				bundle.putInt(BUNDLE_KEY_ANIM_ID_EXIT, R.anim.slide_out_l2r);
			}
		}
	}

    /**
     * Add configuration for screen transition sliding in from right to left.
     *
     * @param bundle The bundle to add config to.
     * @param moveCurrScreenOutTotally If true, the top screen will be moved out totally.
     *        Otherwise only a part of the top screen will be moved out (say 30% of its width).
     */
    public static void addScreenTransitionSlideInR2L(Bundle bundle, boolean moveCurrScreenOutTotally) {
        if (bundle != null) {
            if (moveCurrScreenOutTotally) {
                bundle.putInt(BUNDLE_KEY_ANIM_ID_ENTER, R.anim.slide_in_r2l);
                bundle.putInt(BUNDLE_KEY_ANIM_ID_EXIT, R.anim.slide_out_r2l);
            } else {
                bundle.putInt(BUNDLE_KEY_ANIM_ID_ENTER, R.anim.slide_in_r2l);
                bundle.putInt(BUNDLE_KEY_ANIM_ID_EXIT, R.anim.slide_out_r2l_30p);
            }
        }
    }
}
