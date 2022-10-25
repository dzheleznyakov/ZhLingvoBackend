import React from 'react';
import { useDispatch, useSelector } from 'react-redux';

import NewDictionaryDialog from '../../NewDictionaryDialog/NewDictionaryDialog';
import EditDictionaryDialog from '../../EditDictionaryDialog/EditDictionaryDialog';
import DeleteDictionaryDialog from '../../DeleteDictionaryDialog/DeleteDictionaryDialog';
import { selectedDictionarySelector } from '../../../../store/selectors';
import { DICTIONARY as dictionaryUrlPattern } from '../../../../static/constants/paths';
import * as actions from '../../../../store/actions';
import { ControlBox, MODAL_TYPES } from '../../../UI';

const TableControl = () => {
    const selectedDictionary = useSelector(selectedDictionarySelector);
    const dispath = useDispatch();
    const noDictionarySelected = !selectedDictionary.id;

    const onForward = () => {
        const selectedDictionaryId = selectedDictionary.id;
        const path = dictionaryUrlPattern.replace(/:(\w)+/g, (param) => {
            switch (param) {
                case ':id': return selectedDictionaryId;
                default: return '';
            }
        });
        dispath(actions.navigateTo(path));
    };

    return <ControlBox 
        panelKeyPrefix="dictionary_table_control-"
        disabled={noDictionarySelected}
        panels={[
            {
                modalType: MODAL_TYPES.NEW,
                panel: NewDictionaryDialog,
                disabled: false,
            },
            {
                modalType: MODAL_TYPES.EDIT,
                panel: EditDictionaryDialog,
            },
            {
                modalType: MODAL_TYPES.DELETE,
                panel: DeleteDictionaryDialog,
            },
            {
                modalType: MODAL_TYPES.FORWARD,
                clicked: onForward
            }
        ]}
    />;
};

export default TableControl;
