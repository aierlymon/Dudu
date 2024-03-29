package com.dudu.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.interfaces;

import android.widget.ImageView;

/**
 * 主要功能:
 *
 * @Prject: LoopViewPager
 * @Package: com.example.huodai.widget.jingewenku.abrahamcaijin.loopviewpagers.interfaces
 * @author: Abraham
 * @date: 2019年04月20日 14:05
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */

public interface UpdateImage<T> {

    void loadImage(ImageView view, int position, T item);

}
