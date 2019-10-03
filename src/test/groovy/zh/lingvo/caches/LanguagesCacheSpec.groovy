//package zh.lingvo.caches
//
//import com.google.common.collect.ImmutableList
//import org.junit.runner.RunWith
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.test.context.junit4.SpringRunner
//import spock.lang.Specification
//import zh.lingvo.domain.languages.Language
//
//@SpringBootTest
//class LanguagesCacheSpec extends Specification {
//    @Autowired
//    private LanguagesCache languagesCache;
//
//    def "LanguageCache should load all defined languages"() {
//        when: 'languages are requested'
//        def languages = languagesCache.get()
//
//        then: 'the languages are all as expected'
//        languages.stream()
//                .map(Language.&getCode)
//                .collect(ImmutableList.toImmutableList()) == ['En']
//    }
//}
