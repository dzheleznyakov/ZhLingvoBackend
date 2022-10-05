import React from 'react';
import PropTypes from 'prop-types';
import { useDispatch } from 'react-redux';

import { useActionOnMount } from '../../../../../hooks';
import * as actions from '../../../../../store/actions';
import QuizForm from '../../../QuizForm/QuizForm';

const NewQuizDialog = props => {
    const { close } = props;
    useActionOnMount(actions.fetchAllLanguages());
    const dispatch = useDispatch();

    const confirmed = (name, targetLanguage) =>
        dispatch(actions.createQuiz(name, targetLanguage));

    return (
        <QuizForm 
            title="New Quiz"
            close={close}
            confirmed={confirmed} 
        />
    ); 
};

NewQuizDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default NewQuizDialog;
