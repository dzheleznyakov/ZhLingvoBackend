import React from 'react';
import { useSelector } from 'react-redux';
import { useParams } from 'react-router';

import classes from './DictionaryView.module.scss';

import { Spinner } from '../UI';
import WordList from './WordsList/WordsList';
import WordCard from './WordCard/WordCard';
import * as selectors from '../../store/selectors';
import * as actions from '../../store/actions';
import { useActionOnMount, useDynamicBreadcrumbs } from '../../hooks';

const DictionaryView = () => {
    const { id, wordMainForm } = useParams();
    useActionOnMount(actions.fetchDictionary(+id));

    const dictionary = useSelector(selectors.loadedDictionarySelector);
    const dictionaryName = (dictionary && dictionary.name) || '';
    const breadcrumbs = ['Dictionaries', dictionaryName];
    useDynamicBreadcrumbs([dictionaryName], ...breadcrumbs);
    
    const dictionaryLoading = useSelector(selectors.loadingDictionariesSelector);

    return (
        <div>
            <h1 className={classes.DictionaryName}>{dictionaryName}</h1>
            <h2 className={classes.LanguageName}>{dictionary && dictionary.language.name}</h2>
            {dictionaryLoading && <Spinner />}
            <div className={classes.ContentWrapper}>
                <WordList dictionaryId={id} parentBreadcrumbs={breadcrumbs} />
                <WordCard dictionaryId={id} wordMainForm={wordMainForm} />
            </div>
        </div>
    );
};

export default DictionaryView;
