package com.r_mobile.phasebook;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.r_mobile.phasebook.greenDao.Phrase;
import com.r_mobile.phasebook.greenDao.PhraseDao;

import de.greenrobot.dao.test.AbstractDaoTestLongPk;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends AbstractDaoTestLongPk<PhraseDao, Phrase> {


    public ApplicationTest() {
        super(PhraseDao.class);
    }

    @Override
    protected Phrase createEntity(Long aLong) {
        Phrase phrase1 = new Phrase();
        phrase1.setId(aLong);
        phrase1.setPhrase("Фраза 1");
        phrase1.setFavorite(0);
        phrase1.setOwn(1);
        phrase1.setCategoryId((long) 1);
        return phrase1;
    }

    public void testUpdate() {
        Phrase entity = createEntity((long) 1);
        entity.setFavorite(1);
        entity.setPhrase("Фраза не 1");
        dao.insert(entity);
        entity.setFavorite(1);
        entity.setPhrase("Фраза не 1 а 2");
        dao.update(entity);
        String test = dao.load((long) 1).getPhrase();
        assertEquals(1, (int) entity.getFavorite());
        assertEquals("Фраза не 1 а 2", dao.load((long) 1).getPhrase());
    }

    public void testAdd() {
        Phrase entity = createEntity((long)1);
        dao.insert(entity);
        assertEquals("Фраза 1", dao.load(((long) 1)).getPhrase());
    }

    public void testDelete() {
        Phrase entity = createEntity((long) 1);
        dao.insert(entity);
        assertEquals("Фраза 1", dao.load(((long) 1)).getPhrase());
        dao.delete(entity);
        assertNull(dao.load((long) 1));
    }
}