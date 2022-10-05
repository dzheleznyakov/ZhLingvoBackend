import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch, useSelector } from 'react-redux';

import { selectedQuizSelector } from '../../../../../store/selectors';
import * as actions from '../../../../../store/actions';
import QuizForm from '../../../QuizForm/QuizForm';

const EditQuizDialog = props => {
    const { close } = props;
    const dispatch = useDispatch();
    const quiz = useSelector(selectedQuizSelector);

    const confirmed = (name) => dispatch(actions.updateQuiz(name));

    return (
        <QuizForm 
            title="Edit Quiz"
            close={close}
            confirmed={confirmed}
            quiz={quiz}
            disabledInputs={{ targetLanguage: true }}
        />
    );
};

EditQuizDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default EditQuizDialog;
