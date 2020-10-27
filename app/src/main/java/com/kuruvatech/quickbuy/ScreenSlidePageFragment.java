package com.kuruvatech.quickbuy;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class ScreenSlidePageFragment extends Fragment {
    private static final String PIC_URL = "screenslidepagefragment.picurl";
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public static ScreenSlidePageFragment newInstance(String picUrl) {

       //Resim yollarını Bundle metoduna ekledik
        Bundle arguments = new Bundle();
        arguments.putString(PIC_URL, picUrl);

        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);

        ImageView imageView = (ImageView)rootView.findViewById(R.id.image);

        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress);//


        //ve Resim yollarını aldık
        Bundle arguments = getArguments();
        String url = arguments.getString(PIC_URL);

        //Şimdi Universal Image Loader kütüphanesini kullanarak, resimlerimizi yükleyelim


        //Verilen resim yolunda, resim olmama yada resmin hatalı olma gibi durumlarda, ImageView de default gösterilcek
        //resim iconlarını  tanımladık.
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.logo)
                .showImageForEmptyUri(R.drawable.logo)
                .showImageOnFail(R.drawable.logo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(getActivity()));

        //displayImage metodunu kullanrak resmin yüklenmesinive görüntülenmasini sağladık

        imageLoader.getInstance()
                .displayImage(url, imageView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        progressBar.setProgress(2);
                        progressBar.setVisibility(View.GONE);//Yüklenme işlemi başladığında progressBar iconu görünür yaptık
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        progressBar.setVisibility(View.GONE);//Yüklenme işlemi bittiğinde progressBar iconu görünmez yaptık
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        progressBar.setVisibility(View.GONE);
                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current, int total) {
                        progressBar.setProgress(Math.round(50.0f * current / total));
                    }
                });

        return rootView;
    }
}
