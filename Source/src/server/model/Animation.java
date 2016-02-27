package server.model;

import java.util.HashMap;
import java.util.Map;

public class Animation {

	/**
	 * Creates an animation with no delay.
	 * 
	 * @param id
	 *            The id.
	 * @return The new animation object.
	 */
	public static Animation create(int id) {
		return create(id, 0);
	}

	/**
	 * Creates an animation.
	 * 
	 * @param id
	 *            The id.
	 * @param delay
	 *            The delay.
	 * @return The new animation object.
	 */
	public static Animation create(int id, int delay) {
		return new Animation(id, delay);
	}

	/**
	 * The id.
	 */
	private int id;

	/**
	 * The delay.
	 */
	private int delay;

	/**
	 * Creates an animation.
	 * 
	 * @param id
	 *            The id.
	 * @param delay
	 *            The delay.
	 */
	public Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the delay.
	 * 
	 * @return The delay.
	 */
	public int getDelay() {
		return delay;
	}

	public enum FacialAnimation {
		/**
		 * Dialogue animations.
		 */
		HAPPY(Animation.create(588)), // - Joyful/happy
		CALM_1(Animation.create(589)), // - Speakingly calmly
		CALM_2(Animation.create(590)), // - Calm talk
		DEFAULT(Animation.create(591)), // - Default speech
		EVIL(Animation.create(592)), // - Evil
		EVIL_CONTINUED(Animation.create(593)), // - Evil continued
		DELIGHTED_EVIL(Animation.create(594)), // - Delighted evil
		ANNOYED(Animation.create(595)), // - Annoyed
		DISTRESSED(Animation.create(596)), // - Distressed
		DISTRESSED_CONTINUED(Animation.create(597)), // - Distressed continued
		ALMOST_CRYING(Animation.create(598)), // - Almost crying
		BOWS_HEAD_WHILE_SAD(Animation.create(599)), // - Bows head while sad
		DRUNK_TO_LEFT(Animation.create(600)), // - Talks and looks sleepy/drunk
		// to left
		DRUNK_TO_RIGHT(Animation.create(601)), // - Talks and looks sleepy/drunk
		// to right
		DISINTERESTED(Animation.create(602)), // - Sleepy or disinterested
		SLEEPY(Animation.create(603)), // - Tipping head as if sleepy.
		PLAIN_EVIL(Animation.create(604)), // - Plain evil
		// (Animation.create(Grits teeth and
		// moves eyebrows)
		LAUGH_1(Animation.create(605)), // - Laughing or yawning
		LAUGH_2(Animation.create(606)), // - Laughing or yawning for longer
		LAUGH_3(Animation.create(607)), // - Laughing or yawning for longer
		LAUGH_4(Animation.create(608)), // - Laughing or yawning
		EVIL_LAUGH(Animation.create(609)), // - Evil laugh then plain evil
		SAD(Animation.create(610)), // - Slightly sad
		MORE_SAD(Animation.create(611)), // - Quite sad
		ON_ONE_HAND(Animation.create(612)), // - On one hand...
		NEARLYC_RYING(Animation.create(613)), // - Close to crying
		ANGER_1(Animation.create(614)), // - Angry
		ANGER_2(Animation.create(615)), // - Angry
		ANGER_3(Animation.create(616)), // - Angry
		ANGER_4(Animation.create(617)); // - Angry

		/**
		 * A map of facial animations.
		 */
		private static Map<Animation, FacialAnimation> facialAnimations = new HashMap<Animation, FacialAnimation>();

		/**
		 * Gets a facial animation by its ID.
		 * 
		 * @param facialAnimation
		 *            The facial animation item id.
		 * @return The facial animation, or <code>null</code> if the id is not a
		 *         facial animation.
		 */
		public static FacialAnimation forId(Animation facialAnimation) {
			return facialAnimations.get(facialAnimation);
		}

		/**
		 * Populates the facial animation map.
		 */
		static {
			for (FacialAnimation facialAnimation : FacialAnimation.values()) {
				facialAnimations.put(facialAnimation.animation, facialAnimation);
			}
		}

		private Animation animation;

		private FacialAnimation(Animation animation) {
			this.animation = animation;
		}

		/**
		 * @return the animation
		 */
		public Animation getAnimation() {
			return animation;
		}
	}
}