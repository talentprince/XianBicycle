package org.weyoung.xianbicycle;

import org.weyoung.xianbicycle.ui.bookmark.BookmarkFragment;
import org.weyoung.xianbicycle.ui.search.SearchFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(
    modules = {AppModule.class}
)
public interface AppComponent {
    void inject(BookmarkFragment bookmarkFragment);

    void inject(SearchFragment searchFragment);
}
