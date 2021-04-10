import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './WordCard.module.scss';

import { Spinner } from '../../UI';
import WordView from './WordView/WordView';
import { useConditionalActionOnMount } from '../../../hooks';
import * as selectors from '../../../store/selectors';
import * as actions from '../../../store/actions';

const WordCard = props => {
    const wordIsLoading = useSelector(selectors.wordIsLoadingSelector);

    const { dictionaryId, wordMainForm } = props;

    const wordsList = useSelector(selectors.wordsListSelector);
    const loadedWord = useSelector(selectors.loadedWordSelector) || [];
    const dispatch = useDispatch();

    useConditionalActionOnMount(
        actions.fetchWord(dictionaryId, wordMainForm),
        dictionaryId >= 0 && wordMainForm,
        dictionaryId, wordMainForm);

    const wordIndex = wordsList.indexOf(wordMainForm);
    const wordIsInList = wordIndex >= 0;

    useEffect(() => {
        if (wordIsInList)
            dispatch(actions.selectWord(wordIndex));
    }, [dispatch, wordIndex]);

    const wordViews = loadedWord.map((w, i) => <WordView key={w.id} word={w} index={i} />);

    switch (true) {
        case wordIsLoading: return <Spinner />;
        case wordMainForm && wordIsInList: return (
            <div className={classes.WordCardWrapper}>
                <div className={classes.WordMainForm}>{wordMainForm}</div>
                {wordViews}
            </div>
        );
        default: return null;
    }
};

WordCard.propTypes = {
    dictionaryId: PropTypes.string,
    wordMainForm: PropTypes.string,
};

export default WordCard;
