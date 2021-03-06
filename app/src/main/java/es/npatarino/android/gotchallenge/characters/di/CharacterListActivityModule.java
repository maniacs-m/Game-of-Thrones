package es.npatarino.android.gotchallenge.characters.di;

import dagger.Module;
import dagger.Provides;
import es.npatarino.android.gotchallenge.base.list.usecases.GetListUseCase;
import es.npatarino.android.gotchallenge.characters.domain.CharactersDomain;
import es.npatarino.android.gotchallenge.characters.domain.usecases.GetCharactersByHouseUseCase;
import es.npatarino.android.gotchallenge.characters.domain.model.GoTCharacter;
import es.npatarino.android.gotchallenge.characters.list.presenter.CharacterListByHousePresenter;
import es.npatarino.android.gotchallenge.characters.list.presenter.CharacterListPresenter;
import es.npatarino.android.gotchallenge.common.di.ExecutorThread;
import es.npatarino.android.gotchallenge.common.di.UiThread;
import es.npatarino.android.gotchallenge.common.di.activity.ActivityModule;
import es.npatarino.android.gotchallenge.common.di.activity.ActivityScope;
import rx.Scheduler;

@Module
public class CharacterListActivityModule extends ActivityModule {

    @Provides
    @ActivityScope
    public GetCharactersByHouseUseCase provideCharactersByHouseUseCase(@ExecutorThread Scheduler executor,
                                                                       @UiThread Scheduler uiThread,
                                                                       CharactersDomain.Repository repository) {
        return new GetCharactersByHouseUseCase(repository, uiThread, executor);
    }

    @Provides
    @ActivityScope
    @Character
    public GetListUseCase<GoTCharacter> provideGotCharacterListUseCase(@ExecutorThread Scheduler executor,
                                                                       @UiThread Scheduler uiThread,
                                                                       CharactersDomain.Repository repository) {
        return new GetListUseCase<>(repository, uiThread, executor);
    }

    @Provides
    @ActivityScope
    public CharacterListPresenter
    provideGotCharacterListPresenter(@Character GetListUseCase<GoTCharacter> characterGetListUseCase) {
        return new CharacterListPresenter(characterGetListUseCase);
    }

    @Provides
    @ActivityScope
    public CharacterListByHousePresenter provideGotCharacterListByHousePresenter(GetCharactersByHouseUseCase useCase) {
        return new CharacterListByHousePresenter(useCase);
    }
}
