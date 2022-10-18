import React from 'react';
import PropTypes from 'prop-types';
import { useParams } from 'react-router';
import { useSelector } from 'react-redux';

import classes from './QuizRecordCard.module.scss';

import * as selectors from '../../../store/selectors';
import * as actions from '../../../store/actions';
import { useConditionalActionOnMount } from '../../../hooks';

const QuizRecordCard = props => {
    const { qid, rid } = useParams();
    const quizId = +qid;
    const recordId = +rid;
    const quizIsLoading = useSelector(selectors.quizIsLoadingSelector);

    useConditionalActionOnMount(
        actions.fetchQuizRecord(quizId, recordId),
        !Number.isNaN(quizId) && !Number.isNaN(recordId),
        quizId, recordId);

    return <div>{quizId}: {recordId}</div>;
};

QuizRecordCard.propTypes = {};

QuizRecordCard.defaultProps = {};

export default QuizRecordCard;
