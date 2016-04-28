/*
 * Copyright (C) 2015 A Weyoung App
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.weyoung.xianbicycle.ui.search;

import com.baidu.location.BDLocation;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.weyoung.xianbicycle.data.BicycleResult;

import java.util.List;


interface SearchView extends MvpView {
    void showMessage(int resId);
    void showTip(int resId);
    void setLocation(BDLocation location);
    void showLoading();
    void showContent();
    void setData(List<BicycleResult> data);
}
