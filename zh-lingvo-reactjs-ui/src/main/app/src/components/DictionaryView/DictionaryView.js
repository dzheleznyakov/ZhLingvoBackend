import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router';

import classes from './DictionaryView.module.scss';

import { Spinner } from '../UI';
import WordList from './WordsList/WordsList';
import WordCard from './WordCard/WordCard';
import { BREADCRUMBS_TYPES } from '../../utils/breadcrumbs';
import * as selectors from '../../store/selectors';
import * as actions from '../../store/actions';
import { useActionOnMount, useDynamicBreadcrumbs } from '../../hooks';

const BREADCRUMBS_GETTER = (dictionaryName, toHome) => [{
    type: BREADCRUMBS_TYPES.URL,
    text: 'Dictionaries',
    href: '/dictionaries',
    onClick: toHome,
}, {
    type: BREADCRUMBS_TYPES.TEXT,
    text: dictionaryName,
}];

const DictionaryView = () => {
    const { id, wordMainForm } = useParams();
    const dispatch = useDispatch();
    useActionOnMount(actions.fetchDictionary(+id));

    const dictionary = useSelector(selectors.loadedDictionarySelector);
    const { code: languageCode } = (dictionary || {}).language || {};
    const dictionaryName = (dictionary && dictionary.name) || '';
    const breadcrumbs = BREADCRUMBS_GETTER(
        dictionaryName, 
        () => dispatch(actions.navigateTo('/dictionaries')),
    );
    useDynamicBreadcrumbs([dictionaryName], ...breadcrumbs);

    useEffect(() => {
        if (languageCode)
            dispatch(actions.fetchPos(languageCode));
    }, [languageCode, dispatch]);
    
    const dictionaryLoading = useSelector(selectors.loadingDictionariesSelector);

    return (
        <div>
            <h1 className={classes.DictionaryName}>{dictionaryName}</h1>
            <h2 className={classes.LanguageName}>{dictionary && dictionary.language.name}</h2>
            {dictionaryLoading && <Spinner />}
            <div className={classes.ContentWrapper}>
                <WordList 
                    dictionaryId={id} 
                    dictionaryName={dictionaryName} 
                    parentBreadcrumbs={breadcrumbs} 
                />
                <WordCard 
                    dictionaryId={id} 
                    wordMainForm={wordMainForm} 
                />
            </div>
        </div>
    );
};

export default DictionaryView;
