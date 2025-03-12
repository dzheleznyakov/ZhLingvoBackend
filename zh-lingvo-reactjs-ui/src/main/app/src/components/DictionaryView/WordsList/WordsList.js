import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useParams } from 'react-router';
import PropTypes from 'prop-types';

import classes from './WordsList.module.scss';

import WordListControl from './WordListControl/WordListControl';
import { BREADCRUMBS_TYPES } from '../../../utils/breadcrumbs';
import { useActionOnMount, useDynamicBreadcrumbs } from '../../../hooks';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';
import { ListView } from '../../UI';
import ResusableListView from '../../UI/ListView/ResusableListView';

const URLS = {
    DICTIONARIES: '/dictionaries',
    GET_DICTIONARY_ID: id => `/dictionaries/${id}`,
}

const BREADCRUMBS_GETTER = ({ dictionaryName, dictionaryId, toHome, toDictionary }) => [{
    type: BREADCRUMBS_TYPES.URL,
    text: 'Dictionaries',
    href: URLS.DICTIONARIES,
    onClick: toHome,
}, {
    type: BREADCRUMBS_TYPES.URL,
    text: dictionaryName,
    href: URLS.GET_DICTIONARY_ID(dictionaryId),
    onClick: toDictionary,
}];

const WordsList = props => {
    const { dictionaryId, dictionaryName, parentBreadcrumbs } = props;
    const wordsList = useSelector(selectors.wordsListSelector);
    const dispatch = useDispatch();
    
    useActionOnMount(actions.fetchWordsList(dictionaryId));
    
    const baseParentBreadcrumbs = BREADCRUMBS_GETTER({ 
        dictionaryName, 
        dictionaryId, 
        toHome: () => dispatch(actions.navigateTo(URLS.DICTIONARIES)),
        toDictionary: () => dispatch(actions.navigateTo(URLS.GET_DICTIONARY_ID(dictionaryId))), 
    });

    const [breadcrumbs, setBreadcrumbs] = useState(parentBreadcrumbs);
    
    const { wordMainForm } =  useParams();
    useEffect(() => {
        if (wordMainForm)
            setBreadcrumbs(baseParentBreadcrumbs.concat([{
                type: BREADCRUMBS_TYPES.TEXT,
                text: wordMainForm,
            }]));
        else
            setBreadcrumbs(parentBreadcrumbs);
    }, [wordMainForm, dictionaryId, dictionaryName, parentBreadcrumbs, setBreadcrumbs]);

    useDynamicBreadcrumbs([breadcrumbs], ...breadcrumbs);

    const wrapperClasses = [classes.WordsListWrapper];
    if (wordMainForm)
        wrapperClasses.push(classes.Active);

    const items = wordsList.map(word => ({ key: word, node: word }));
    const onWordClick = index => () => {
        dispatch(actions.selectWord(index));
        const wordMainForm = wordsList[index];
        dispatch(actions.navigateTo(`/dictionaries/${dictionaryId}/${wordMainForm}`));
    };
    
    const selectedIndex = wordMainForm 
        ? wordsList.indexOf(wordMainForm)
        : -1;

    const listView = items.length === 0 
        ? null 
        : <ResusableListView
            items={items} 
            onItemClick={onWordClick} 
            selectedIndex={selectedIndex} />;

    return (
        <div className={wrapperClasses.join(' ')}>
            {listView}
            <WordListControl />
        </div>
    );
};

WordsList.propTypes = {
    dictionaryId: PropTypes.string.isRequired,
    dictionaryName: PropTypes.string.isRequired,
    parentBreadcrumbs: PropTypes.arrayOf(PropTypes.object).isRequired,
};

export default WordsList;
