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
package org.weyoung.xianbicycle.utils;

import android.content.Context;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.SavedRevision;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.google.common.collect.ImmutableMap;

import org.greenrobot.eventbus.EventBus;
import org.weyoung.xianbicycle.ui.bookmark.BookmarkFragment;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

public class BookmarkUtil {

    private static final String DATABASE_NAME = "xianbicycle.db";
    public static final String SITE_ID = "siteId";
    private static final String VIEW_NAME = "viewById";
    private LiveQuery liveQuery;
    private Manager manager;
    private Database database;
    private View view;

    public BookmarkUtil(Context context) {
        try {
            manager = new Manager(new AndroidContext(context), Manager.DEFAULT_OPTIONS);
            database = manager.getDatabase(DATABASE_NAME);
            view = database.getView(VIEW_NAME);
            view.setMap(new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    Object siteId = document.get(SITE_ID);
                    if (siteId != null) {
                        emitter.emit(siteId.toString(), null);
                    }
                }
            }, "1");
            liveQuery = view.createQuery().toLiveQuery();
            liveQuery.addChangeListener(new LiveQuery.ChangeListener() {
                @Override
                public void changed(LiveQuery.ChangeEvent event) {
                    EventBus.getDefault().post(new BookmarkFragment.BookmarkUpdate());
                }
            });
            liveQuery.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSiteId(String id) {
        Document document = database.createDocument();
        try {
            document.putProperties(newHashMap(ImmutableMap.of(SITE_ID, (Object)id)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSiteId(String id) {
        Query query = view.createQuery();
        query.setStartKey(id);
        query.setEndKey(id);
        query.setLimit(1);
        try {
            QueryEnumerator run = query.run();
            if (run.hasNext()) {
                QueryRow next = run.next();
                next.getDocument().delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> queryAllIds() {
        List<String> ids = newArrayList();
        Query query = view.createQuery();
        query.setDescending(true);
        try {
            QueryEnumerator run = query.run();
            for (Iterator<QueryRow> it = run; it.hasNext(); ) {
                QueryRow next = it.next();
                SavedRevision currentRevision = next.getDocument().getCurrentRevision();
                ids.add((String) currentRevision.getProperty(SITE_ID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public boolean contains(String id) {
        Query query = view.createQuery();
        query.setStartKey(id);
        query.setEndKey(id);
        query.setLimit(1);
        try {
            QueryEnumerator run = query.run();
            return run.hasNext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
