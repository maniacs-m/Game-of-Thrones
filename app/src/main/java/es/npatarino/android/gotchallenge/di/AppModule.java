package es.npatarino.android.gotchallenge.di;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import es.npatarino.android.gotchallenge.data.CharacterRepositoryImp;
import es.npatarino.android.gotchallenge.data.caching.TimeProvider;
import es.npatarino.android.gotchallenge.data.caching.strategy.TTLCachingStrategy;
import es.npatarino.android.gotchallenge.data.source.local.CharacterLocalDataSourceImp;
import es.npatarino.android.gotchallenge.data.source.remote.CharacterRemoteDataSourceImp;
import es.npatarino.android.gotchallenge.data.source.remote.EndPoint;
import es.npatarino.android.gotchallenge.data.source.remote.JsonMapper;
import es.npatarino.android.gotchallenge.domain.datasource.local.CharacterLocalDataSource;
import es.npatarino.android.gotchallenge.domain.datasource.remote.CharacterRemoteDataSource;
import es.npatarino.android.gotchallenge.domain.repository.CharacterRepository;
import okhttp3.OkHttpClient;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@Module
public class AppModule {

    private static final String END_POINT = "https://raw.githubusercontent.com/tonilopezmr/Game-of-Thrones/master/app/src/test/resources/data.json";

    @Provides
    @Singleton
    public JsonMapper provideGotCharacterJsonMapper(){
        return new JsonMapper(new Gson());
    }

    @Provides
    @Singleton
    public CharacterLocalDataSource provideCharacterLocalDataSource() {
        return new CharacterLocalDataSourceImp(new TTLCachingStrategy(2, TimeUnit.MINUTES), new TimeProvider());
    }

    @Provides
    @Singleton
    public CharacterRemoteDataSource provideCharacterRemoteDataSource(OkHttpClient okHttpClient,
                                                                      EndPoint endPoint,
                                                                      JsonMapper jsonMapper) {
        return new CharacterRemoteDataSourceImp(jsonMapper, endPoint, okHttpClient);
    }


    @Provides
    @Singleton
    public CharacterRepository provideGotCharacterRepository(CharacterRemoteDataSource remoteDataSource,
                                                             CharacterLocalDataSource localDataSource) {
        return new CharacterRepositoryImp(remoteDataSource, localDataSource);
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(){
        return new OkHttpClient();
    }

    @Provides @Named("executorThread")
    public Scheduler provideMainThread(){
        return Schedulers.newThread();
    }

    @Provides @Named("mainThread")
    public Scheduler provideExecutor(){
        return AndroidSchedulers.mainThread();
    }

    @Provides
    public EndPoint provideEndPoint(){
        return new EndPoint(END_POINT);
    }
}
