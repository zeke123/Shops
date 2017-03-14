package krelve.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.ArrayList;
import java.util.List;



public class Kanner extends FrameLayout
{
    private int count;

    private String[] imagesUrls;
    private ImageLoader mImageLoader;
    private List<ImageView> imageViews;
    private Context context;
    private ViewPager vp;
    private int postion;
    private DisplayImageOptions options;
    public Kanner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initImageLoader(context);
        initData();
    }
    public Kanner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public Kanner(Context context) {
        this(context, null);
    }
    private void initData() {
        imageViews = new ArrayList<ImageView>();
    }

    public void setImagesUrl(String[] imagesUrl,int postion) {


        this.postion = postion;
        initLayout();
        initImgFromNet(imagesUrl);
        showTime();
    }

    private void initLayout()
    {
        imageViews.clear();
        View view = LayoutInflater.from(context).inflate(R.layout.kanner_layout, this, true);
        vp = (ViewPager) view.findViewById(R.id.vp);
    }

    private void initImgFromNet(String[] imagesUrl) {

        imagesUrls = imagesUrl;

        count = imagesUrl.length;
        // 添加事件监听
        initListener();

      /*  for (int i = 0; i < count; i++) {
            iv = new ImageView(context);
            iv.setScaleType(ScaleType.FIT_XY);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bm = BitmapFactory.decodeFile(imagesUrl[i], options);
            iv.setImageBitmap(bm);
            imageViews.add(iv);
        }*/


    }

    private void initListener()
    {
        vp.setOnPageChangeListener(new OnPageChangeListener()
        {
            boolean isScrolled = false;
            @Override
            public void onPageScrollStateChanged(int status)
            {
                switch (status)
                {
                    case 1:// 手势滑动
                        isScrolled = false;
                        break;
                    case 2:// 界面切换
                        isScrolled = true;
                        break;
                    case 0:// 滑动结束
                        // 当前为最后一张，此时从右向左滑，则切换到第一张
                        if (vp.getCurrentItem() == vp.getAdapter().getCount() - 1 && !isScrolled)
                        {
                            vp.setCurrentItem(0);
                        }
                        // 当前为第一张，此时从左向右滑，则切换到最后一张
                        else if (vp.getCurrentItem() == 0 && !isScrolled)
                        {
                            vp.setCurrentItem(vp.getAdapter().getCount() - 1);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageSelected(int index)
            {

            }
        });
    }

    private void showTime()
    {
        vp.setAdapter(new KannerPagerAdapter());
        vp.setCurrentItem(postion);
    }


    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        mImageLoader = ImageLoader.getInstance();
    }

    class KannerPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount() {
            return imagesUrls.length;
        }

        @Override
        public int getItemPosition(Object object) {

            if (object != null && imagesUrls != null) {
                String resId = (String)((ImageView)object).getTag();
                if (resId != null) {
                    for (int i = 0; i < imagesUrls.length; i++) {
                        if (resId.equals(imagesUrls[i])) {
                            return i;
                        }
                    }
                }
            }

            return POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1)
        {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (imagesUrls != null && position < imagesUrls.length) {
                String resId = imagesUrls[position];
                if (resId != null) {
                    ImageView itemView = new ImageView(context);
                    itemView.setScaleType(ImageView.ScaleType.FIT_XY);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap bm = BitmapFactory.decodeFile(resId, options);
                    itemView.setImageBitmap(bm);
                    //此处假设所有的照片都不同，用resId唯一标识一个itemView；也可用其它Object来标识，只要保证唯一即可
                    itemView.setTag(resId);
                    ((ViewPager) container).addView(itemView);
                    return itemView;
                }
            }
            return null;
           // ((ViewPager)container).addView(imageViews.get(position % imageViews.size()), 0);
           // return imageViews.get(position % imageViews.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object != null) {
                ViewGroup viewPager = ((ViewGroup) container);
                int count = viewPager.getChildCount();
                for (int i = 0; i < count; i++) {
                    View childView = viewPager.getChildAt(i);
                    if (childView == object) {
                        viewPager.removeView(childView);
                        break;
                    }
                }
            }

           // ((ViewPager)container).removeView(imageViews.get(position % imageViews.size()));
        }
    }
}