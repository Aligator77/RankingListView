package alex.rankinglist.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import alex.rankinglist.databinding.WidgetRankingListBinding;
import alex.rankinglist.widget.model.Rank;
import alex.rankinglist.widget.model.RankGroup;
import alex.rankinglist.widget.model.User;


public class RankingListView extends ScrollView {
	WidgetRankingListBinding binding;
	ScaleGestureDetector scaleDetector;

	public RankingListView(Context context) {
		super(context);
		init();
	}

	public RankingListView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RankingListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		binding = WidgetRankingListBinding.inflate(LayoutInflater.from(getContext()), this, true);
		scaleDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
	}

	private void fillRankingList(List<RankGroup> rankGroups) {
		for (RankGroup group : rankGroups) {
			RankGroupView groupView = new RankGroupView(getContext());
			groupView.setModel(group);
			binding.listRankingViews.addView(groupView);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		scaleDetector.onTouchEvent(ev);
		return super.onTouchEvent(ev);
	}

	public void setModel(List<Rank> ranks, List<User> users) {
		List<RankGroup> rankGroups = new ArrayList<>();
		int prevRankScoreMax = -1; // to include users with score 0
		for (Rank rank : ranks) {
			List<User> rankedUsers = new ArrayList<>();
			for (User user : users) {
				if (user.score > prevRankScoreMax && user.score <= rank.scoreMax) {
					rankedUsers.add(user);
				}
			}

			rankGroups.add(new RankGroup(rank, rankedUsers));
			prevRankScoreMax = rank.scoreMax;
		}

		fillRankingList(rankGroups);
	}

	class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			// TODO: 26.01.2017 increase factor
			View rootViewGroup = binding.listRankingViews;
			ViewGroup.LayoutParams params = rootViewGroup.getLayoutParams();
			float scaleFactor = detector.getScaleFactor();
			params.height = (int) (rootViewGroup.getHeight() * scaleFactor);
			rootViewGroup.setLayoutParams(params);
			return true;
		}
	}
}