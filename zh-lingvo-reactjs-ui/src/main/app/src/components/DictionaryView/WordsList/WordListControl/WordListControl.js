import React from 'react';
import { useParams } from 'react-router-dom';

import NewWordDialog from '../NewWordDialog/NewWordDialog';
import DeleteWordDialog from '../DeleteWordDialog/DeleteWordDialog';
import ControlBox, { MODAL_TYPES } from '../../../Common/ControlBox/ControlBox';

const WordListControl = () => {
    const { wordMainForm } = useParams();
    const nothingIsSelected = !wordMainForm;

    return <ControlBox
        panelKeyPrefix="word_list_control-"
        disabled={nothingIsSelected}
        panels={[
            {
                modalType: MODAL_TYPES.NEW,
                panel: NewWordDialog,
            },
            {
                modalType: MODAL_TYPES.DELETE,
                panel: DeleteWordDialog,
            },
        ]}
    />;
};

export default WordListControl;
