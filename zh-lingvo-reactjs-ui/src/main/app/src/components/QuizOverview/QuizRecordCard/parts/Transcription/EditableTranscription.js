import React from 'react';
import PropTypes from 'prop-types';
import { useSelector } from 'react-redux';
import { quizRecordIsEditingSelector } from '../../../../../store/selectors';
import NewPartButton from '../Editable/NewPartButton/NewPartButton';
import { TRANSCRIPTION_NEW, TRANSCRIPTION_EDIT, TRANSCRIPTION_DELETE } from '../../../../../static/constants/quizRecordEditModalTypes';
import OnHoverEditable from '../Editable/OnHoverEditable';
import Transcription from './Transcription';

const EditableTranscription = props => {
    const { children: transcription, path } = props;
    const isEditing = useSelector(quizRecordIsEditingSelector);

    if (isEditing && !transcription) {
        return <NewPartButton 
            label="transcription"
            modalType={TRANSCRIPTION_NEW}
            path={path}
        />;
    }

    return (
        <OnHoverEditable
            editModalType={TRANSCRIPTION_EDIT}
            deleteModalType={TRANSCRIPTION_DELETE}
            path={path}
            block={true}
        >
            <Transcription>{transcription}</Transcription>
        </OnHoverEditable>
    );
};

EditableTranscription.propTypes = {
    children: PropTypes.string,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
};


export default EditableTranscription;
