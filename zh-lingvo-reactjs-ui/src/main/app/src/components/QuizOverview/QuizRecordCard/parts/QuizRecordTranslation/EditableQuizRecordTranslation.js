import React from 'react';
import PropTypes from 'prop-types';

import { quizRecordTranslationType } from '../../types';
import NewPartButton from '../Editable/NewPartButton/NewPartButton';
import { TRANSLATION_DELETE, TRANSLATION_EDIT, TRANSLATION_NEW } from '../../../../../static/constants/quizRecordEditModalTypes';
import OnHoverEditable from '../Editable/OnHoverEditable';
import QuizRecordTranslation from './QuizRecordTranslation';

export const NULL_QUIZ_RECORD_TRANSLATION = { id: -1, value: '' };

const EditableQuizRecordTranslation = props => {
    const { entry: translation, path, postfix } = props;

    if (translation === NULL_QUIZ_RECORD_TRANSLATION)
        return <NewPartButton 
            label="translation"
            modalType={TRANSLATION_NEW}
            path={path}
        />;

    return translation && (
        <OnHoverEditable
            editModalType={TRANSLATION_EDIT}
            deleteModalType={TRANSLATION_DELETE}
            path={path}
        >
            <QuizRecordTranslation 
                entry={translation} 
                postfix={postfix} 
            />
        </OnHoverEditable>
    );
};

EditableQuizRecordTranslation.propTypes = {
    entry: quizRecordTranslationType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
    postfix: PropTypes.string,
};


export default EditableQuizRecordTranslation;
