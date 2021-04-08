import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './WordsList.module.scss';

import { useActionOnMount } from '../../../hooks';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';

const WordsList = props => {
    const { dictionaryId } = props;
    useActionOnMount(actions.fetchWordsList(dictionaryId));

    const wordsList = useSelector(selectors.wordsListSelector);
    const selectedWordIndex = useSelector(selectors.selectedWordIndexSelector);
    const dispatch = useDispatch();

    const onWordClick = index => () => dispatch(actions.selectWord(index));
    const getClassName = index => (index === selectedWordIndex ? classes.SelectedWord : null);

    return (
        <ul className={classes.WordsListWrapper}>
            {wordsList.map((word, i) => (
                <li 
                    key={word}
                    className={getClassName(i)}
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
};

export default WordsList;
