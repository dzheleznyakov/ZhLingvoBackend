import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import * as actions from '../../../../../store/actions';
import { selectedQuizSelector } from '../../../../../store/selectors';
import { Dialog } from '../../../../UI';

const DeleteQuizDialog = props => {
    const { close } = props;
    const dispatch = useDispatch();
    const quiz = useSelector(selectedQuizSelector);

    const quizId = (quiz && quiz.id) || -1;
    const confirmed = () => dispatch(actions.deleteQuiz(quizId));

    const text = quiz && quiz.targetLanguage
        ? `Are you sure you want to delete the ${quiz.targetLanguage.name} quiz "${quiz.name}"?`
        : null;

    return (
        <Dialog close={close} confirmed={confirmed}>
            <div>{text}</div>
        </Dialog>
    );
};

DeleteQuizDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default DeleteQuizDialog;
