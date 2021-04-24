import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';
import _ from 'lodash';

import classes from './WordCard.module.scss';

import { Spinner } from '../../UI';
import { WordMainForm } from './WordView/SubWordParts';
import WordView from './WordView/WordView';
import WordViewControl from './WordViewControl/WordViewControl';
import WordEditModal from './WordEditModal/WordEditModal';
import { useConditionalActionOnMount } from '../../../hooks';
import * as selectors from '../../../store/selectors';
import * as actions from '../../../store/actions';

const WordCard = props => {
    const wordIsLoading = useSelector(selectors.wordIsLoadingSelector);

    const { dictionaryId, wordMainForm } = props;

    const wordsList = useSelector(selectors.wordsListSelector);
    const loadedWord = useSelector(selectors.loadedWordSelector) || [];
    const updatedWord = useSelector(selectors.updatedWordSelector) || [];
    const isEditing = useSelector(selectors.isEditingSelector);
    const dispatch = useDispatch();

    const word = isEditing ? updatedWord : loadedWord;

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

    const wordViews = word.map((w, i) => <WordView key={w.id} word={w} index={i} />);

    switch (true) {
        case wordIsLoading: return <Spinner />;
        case wordMainForm && wordIsInList: return (
            <div className={classes.WordCardWrapper}>
                <div className={classes.WordViewWrapper}>
                    <WordMainForm>{_.get(word, '[0].mainForm', wordMainForm)}</WordMainForm>
                    {wordViews}
                </div>
                <WordViewControl />
                <WordEditModal />
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
