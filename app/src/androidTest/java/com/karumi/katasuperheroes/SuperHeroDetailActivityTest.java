package com.karumi.katasuperheroes;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import com.karumi.katasuperheroes.di.MainComponent;
import com.karumi.katasuperheroes.di.MainModule;
import com.karumi.katasuperheroes.model.SuperHero;
import com.karumi.katasuperheroes.model.SuperHeroesRepository;
import com.karumi.katasuperheroes.ui.view.SuperHeroDetailActivity;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.karumi.katasuperheroes.matchers.ToolbarMatcher.onToolbarWithTitle;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class) @LargeTest public class SuperHeroDetailActivityTest {

  public static final String EXTRA_HERO_NAME = "super_hero_name_key";
  public static final String HERO_NAME = "Paco";
  public static final String HERO_DESCRIPTION = "Paco SuperHero description";
  public static final String HERO_PHOTO_URL = "https://i.annihil.us/u/prod/marvel/i/mg/9/b0/537bc2375dfb9.jpg";

  @Rule public DaggerMockRule<MainComponent> daggerRule =
      new DaggerMockRule<>(MainComponent.class, new MainModule()).set(
          new DaggerMockRule.ComponentSetter<MainComponent>() {
            @Override public void setComponent(MainComponent component) {
              SuperHeroesApplication app =
                  (SuperHeroesApplication) InstrumentationRegistry.getInstrumentation()
                      .getTargetContext()
                      .getApplicationContext();
              app.setComponent(component);
            }
          });

  @Rule public IntentsTestRule<SuperHeroDetailActivity> activityRule =
      new IntentsTestRule<>(SuperHeroDetailActivity.class, true, false);

  @Mock SuperHeroesRepository repository;

  @Test public void showsSuperHeroNameOnToolbar() {
    givenThereAreOneSuperHero();

    startActivity();

    onToolbarWithTitle(HERO_NAME).check(matches(isDisplayed()));
  }

  @Test public void showsSuperHeroDescription() {
    givenThereAreOneSuperHero();

    startActivity();

    onView(withId(R.id.tv_super_hero_description)).check(matches(
        allOf(
            ViewMatchers.isDisplayed(),
            ViewMatchers.withText(HERO_DESCRIPTION)
            )
    ));
  }

  @Test public void showsSuperHeroNameOnTitle() {
    givenThereAreOneSuperHero();

    startActivity();

    onView(withId(R.id.tv_super_hero_name)).check(matches(
        allOf(
            ViewMatchers.isDisplayed(),
            ViewMatchers.withText(HERO_NAME)
        )
    ));
  }

  private void givenThereAreOneSuperHero() {
    SuperHero hero = new SuperHero(
        HERO_NAME,
        HERO_PHOTO_URL,
        true,
        HERO_DESCRIPTION
    );
    when(repository.getByName(HERO_NAME)).thenReturn(hero);
  }

  private SuperHeroDetailActivity startActivity() {
    Intent intent = new Intent().putExtra(EXTRA_HERO_NAME, HERO_NAME);
    return activityRule.launchActivity(intent);
  }
}