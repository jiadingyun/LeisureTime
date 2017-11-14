package com.yaping.leisureTime.recyclerViewUtil;

import android.view.View;

/**
 * 功能：监听LongClick事件的回调接口
 *
 * @创建： Created by yaping on 2017/11/13 0013.
 */
public interface OnItemLongClickListener {
  /**
   * 回调函数
   * @param view
   * @param position
   */
  void OnItemLongClick(View view,int position);
}
