package com.payline.payment.docapost.service;

import com.payline.payment.docapost.service.impl.ConfigurationServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith( MockitoJUnitRunner.class )
public class ConfigurationServiceImplTest {

    @InjectMocks
    private ConfigurationServiceImpl service;

    @Test
    public void test() {
        Assert.assertTrue(true);
    }

//    @Test
//    public void testCheck_ok(){
//        // TODO
//        // given: valid contract properties
//
//        // when: checking configuration fields values
//
//        // then: result contains no error
//        Assert.assertTrue( false );
//    }
//
//    @Test
//    public void testCheck_wrongAccountData(){
//        // TODO
//        // given: contract properties with the right format but not valid
//
//        // when: checking configuration fields values
//
//        // then: result contains error(s)
//        Assert.assertTrue( false );
//    }
//
//    @Test
//    public void testCheck_unknownError(){
//        // given: contract properties validation encounter an unexpected error (Server unavailable for example)
//
//        // when: checking configuration fields values
//
//        // then: result contains error(s)
//        Assert.assertTrue( false );
//    }
//
//    @Test
//    public void testGetParameters(){
//        // when: recovering contract parameters
//        List<AbstractParameter> parameters = service.getParameters( Locale.FRANCE );
//
//        // then: exactly ?? parameters are returned
//        Assert.assertNotNull( parameters );
//        Assert.assertEquals( 1, parameters.size() );
//    }
//
//    @Test
//    public void testGetReleaseInformation_ok(){
//        // when: getReleaseInformation method is called
//        ReleaseInformation releaseInformation = service.getReleaseInformation();
//
//        // then: result is not null
//        Assert.assertNotNull( releaseInformation );
//        Assert.assertNotNull( releaseInformation.getVersion() );
//        //Assert.assertNotEquals( "unknown", releaseInformation.getVersion() );
//        Assert.assertNotNull( releaseInformation.getDate() );
//        //Assert.assertNotEquals( 1900, releaseInformation.getDate().getYear() );
//    }
//
//    @Test
//    public void testGetReleaseInformation_versionFormat(){
//        // when: getReleaseInformation method is called
//        ReleaseInformation releaseInformation = service.getReleaseInformation();
//
//        // then: the version has a valid format
//        Assert.assertNotNull( releaseInformation );
//        Assert.assertTrue( releaseInformation.getVersion().matches( "^\\d\\.\\d(\\.\\d)?$" ) );
//    }
//
//    @Test
//    public void testGetName_notNull(){
//        // when: getReleaseInformation method is called
//        String name = service.getName( Locale.FRANCE );
//
//        // then: result is not null and not empty
//        Assert.assertNotNull( name );
//        Assert.assertFalse( name.isEmpty() );
//    }

}
