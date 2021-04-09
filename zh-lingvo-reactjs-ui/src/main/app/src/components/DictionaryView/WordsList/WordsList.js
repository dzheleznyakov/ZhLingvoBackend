import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router';
import PropTypes from 'prop-types';

import classes from './WordsList.module.scss';

import { useActionOnMount, useDynamicBreadcrumbs } from '../../../hooks';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';

const WordsList = props => {
    const { dictionaryId, parentBreadcrumbs } = props;
    useActionOnMount(actions.fetchWordsList(dictionaryId));
    const [breadcrumbs, setBreadcrumbs] = useState(parentBreadcrumbs);

    useDynamicBreadcrumbs([breadcrumbs], ...breadcrumbs);

    const wordsList = useSelector(selectors.wordsListSelector);
    const selectedWordIndex = useSelector(selectors.selectedWordIndexSelector);
    const wrapperClasses = [classes.WordsListWrapper];
    if (selectedWordIndex >= 0)
        wrapperClasses.push(classes.Active);

    const dispatch = useDispatch();
    const history = useHistory();

    const onWordClick = index => () => {
        dispatch(actions.selectWord(index));
        const wordMainForm = wordsList[index];
        history.replace(`/dictionaries/${dictionaryId}/${wordMainForm}`);
        setBreadcrumbs(parentBreadcrumbs.concat([wordMainForm]));
    };
    const getItemClassName = index => (index === selectedWordIndex ? classes.SelectedWord : null);

    return (
        <ul className={wrapperClasses.join(' ')}>
            {wordsList.map((word, i) => (
                <li 
                    key={word}
                    className={getItemClassName(i)}
                    onClick={onWordClick(i)}
                >
                    {word}
                </li>
            ))}
        </ul>
    );
};

WordsList.propTypes = {
    dictionaryId: PropTypes.string.isRequired,
    parentBreadcrumbs: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default WordsList;
