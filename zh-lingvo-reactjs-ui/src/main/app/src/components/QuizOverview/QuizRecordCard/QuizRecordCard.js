import React from 'react';
import PropTypes from 'prop-types';
import { useParams } from 'react-router';
import { useSelector } from 'react-redux';

import classes from './QuizRecordCard.module.scss';

import * as selectors from '../../../store/selectors';

const QuizRecordCard = props => {
    const { id: quizId } = useParams();
    const quizIsLoading = useSelector(selectors.quizIsLoadingSelector);

    return null;
};

QuizRecordCard.propTypes = {};

QuizRecordCard.defaultProps = {};

export default QuizRecordCard;
