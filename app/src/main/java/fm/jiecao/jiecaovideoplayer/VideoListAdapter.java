package fm.jiecao.jiecaovideoplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.listener.JCMediaPlayerListener;
import fm.jiecao.jcvideoplayer_lib.listener.imp.SimpleJCPlayerStateListener;
import fm.jiecao.jcvideoplayer_lib.manager.JCVideoPlayerManager;

/**
 * Created by Nathen
 * On 2016/02/07 01:20
 */
public class VideoListAdapter extends BaseAdapter {

    public static final String TAG = "JieCaoVideoPlayer";

    Context context;

    boolean enableTiny;

    public VideoListAdapter(Context context) {
        this.context = context;
    }

    public VideoListAdapter(Context context, boolean enableTiny) {
        this.context = context;
        this.enableTiny = enableTiny;
    }

    @Override
    public int getCount() {
        return VideoConstant.videoUrls.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.e(TAG, "why you always getview");

        final ViewHolder viewHolder;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_videoview, null);
            viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
            viewHolder.jcVideoPlayer.setEnableTiny(enableTiny);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.jcVideoPlayer.setUp(
                VideoConstant.videoUrls[position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                VideoConstant.videoTitles[position]);

        Picasso.with(convertView.getContext())
                .load(VideoConstant.videoThumbs[position])
                .into(viewHolder.jcVideoPlayer.thumbImageView);

        viewHolder.jcVideoPlayer.setPlayerStateListener(new SimpleJCPlayerStateListener() {
            @Override
            public boolean keepCurViewAlive() {
                JCMediaPlayerListener first = JCVideoPlayerManager.getFirst();
                if (first != null && first instanceof JCVideoPlayerStandard) {
                    JCVideoPlayerStandard jcVideoPlayer = (JCVideoPlayerStandard) first;
                    jcVideoPlayer.setUp(
                            VideoConstant.videoUrls[position + 1], jcVideoPlayer.getScreenType(),
                            VideoConstant.videoTitles[position + 1]);
                    jcVideoPlayer.startButton.performClick();
                    Picasso.with(jcVideoPlayer.getContext())
                            .load(VideoConstant.videoThumbs[position + 1])
                            .into(jcVideoPlayer.thumbImageView);
                    return true;
                }
                return false;
            }
        });
        return convertView;
    }

    class ViewHolder {
        JCVideoPlayerStandard jcVideoPlayer;
    }
}
