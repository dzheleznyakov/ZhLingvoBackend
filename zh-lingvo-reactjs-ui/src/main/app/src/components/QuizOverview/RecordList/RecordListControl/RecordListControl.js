import React from 'react';
import { useParams } from 'react-router-dom';
import ControlBox, { MODAL_TYPES } from '../../../Common/ControlBox/ControlBox';

import DeleteRecordDialog from './DeleteRecordDialog/DeleteRecordDialog';
import NewRecordDialog from './NewRecordDialog/NewRecordDialog';


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
            },
            {
                modalType: MODAL_TYPES.DELETE,
                panel: DeleteRecordDialog,
            },
        ]}
    />;
};

export default RecordListControl;
