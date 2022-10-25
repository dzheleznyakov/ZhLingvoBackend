import React from 'react';
import { useParams } from 'react-router-dom';
import { ControlBox, MODAL_TYPES } from '../../../UI';

import DeleteRecordDialog from './DeleteRecordDialog/DeleteRecordDialog';
import NewRecordDialog from './NewRecordDialog/NewRecordDialog';
import QuizSettingsDialog from './QuizSettingsDialog/QuizSettingsDialog';


const RecordListControl = () => {
    const { rid: recordId } = useParams();
    const noRecordSelected = recordId === null || recordId === undefined;

    return <ControlBox
        panelKeyPrefix="record_list_control-"
        disabled={noRecordSelected}
        panels={[
            {
                modalType: MODAL_TYPES.NEW,
                panel: NewRecordDialog,
                disabled: false,
            },
            {
                modalType: MODAL_TYPES.SETTINGS,
                panel: QuizSettingsDialog,
                disabled: false,
            },
            {
                modalType: MODAL_TYPES.DELETE,
                panel: DeleteRecordDialog,
            },
        ]}
    />;
};

export default RecordListControl;
