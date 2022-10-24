import React from 'react';
import PropTypes from 'prop-types';
import { useParams } from 'react-router-dom';
import QuizSettingsDialogBase from '../../../QuizSettingsDialogBase/QuizSettingsDialogBase';

const QuizSettingsDialog = props => {
    const { close } = props;
    const { qid: quizId } = useParams();
    
    return <QuizSettingsDialogBase
        close={close}
        quizId={+quizId}
    />;
};

QuizSettingsDialog.propTypes = {
    close: PropTypes.func.isRequired,
};

export default QuizSettingsDialog;
