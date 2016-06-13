package es.npatarino.android.gotchallenge.presenter;

import java.util.List;

import es.npatarino.android.gotchallenge.domain.Character;
import es.npatarino.android.gotchallenge.domain.House;
import es.npatarino.android.gotchallenge.domain.interactor.GetCharactersByHouseUseCase;
import es.npatarino.android.gotchallenge.view.DetailView;
import rx.Subscription;

public class CharacterListByHousePresenterImp implements CharacterListByHousePresenter {

    private DetailView<List<Character>> view;
    private GetCharactersByHouseUseCase useCase;

    private Subscription charactersSubscription;

    public CharacterListByHousePresenterImp(GetCharactersByHouseUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void init() {
        view.initUi();
    }

    @Override
    public void setView(DetailView<List<Character>> view) {
        if (view == null) new IllegalArgumentException("oh my god... you are **");
        this.view = view;
    }

    @Override
    public void onDestroy() {
        charactersSubscription.unsubscribe();
    }

    @Override
    public void init(House viewModel) {
        init();
        askForCharacters(viewModel);
    }

    private void askForCharacters(House viewModel) {
        charactersSubscription = useCase.execute(viewModel)
                .subscribe(this::onCharactersReceived, this::onError);
    }

    private void onCharactersReceived(List<Character> characters){
        view.show(characters);
    }

    private void onError(Throwable error){
        view.error();
    }
}