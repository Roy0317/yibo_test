package com.yibo.yiboapp.ui;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Point;

import com.yibo.yiboapp.utils.Utils;

public class BallonAnimator {

	Point start;
	Point end;
	Point control;
	Context context;

	public BallonAnimator(Context context) {
		this.context = context;
	}
	public void init(Point start, Point end) {
		this.start = start;
		this.end = end;
	}

	private void calcControlPoint() {
		int pointX = (start.x + end.x) / 2;
		int pointY = (int) (start.y - Utils.dip2px(context,100));
		control = new Point(pointX, pointY);
	}

	public class BezierEvaluator implements TypeEvaluator<Point> {

		private Point controllPoint;
		public BezierEvaluator(Point controllPoint) {
			this.controllPoint = controllPoint;
		}

		@Override
		public Point evaluate(float t, Point startValue, Point endValue) {
			int x = (int) ((1 - t) * (1 - t) * startValue.x + 2 * t * (1 - t) * controllPoint.x + t * t * endValue.x);
			int y = (int) ((1 - t) * (1 - t) * startValue.y + 2 * t * (1 - t) * controllPoint.y + t * t * endValue.y);
			return new Point(x, y);
		}
	}



}
