import React, { useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import { Form, formInputTypes, validators } from '../../../UI';
import { useAutofocus } from '../../../../hooks';
import * as actions from '../../../../store/actions';
import * as selectors from '../../../../store/selectors';

const RemarkEditDialog = props => {
    const { editting } = props;
    const wordEditPath = useSelector(selectors.wordEditPathSelector);
    const remark = useSelector(selectors.stringPropertyToUpdateSelectorFactory(wordEditPath));
    const dispatch = useDispatch();

    const remarkGroup = {
        key: 'remarkGroup',
        label: editting ? 'Update Remark' : 'New Remark',
    };

    const remarkRef = useRef();
    const remarkField = {
        key: 'remarkField',
        label: 'Remark',
        type: formInputTypes.TEXT,
        defaultValue: remark || '',
        groupKey: remarkGroup.key,
        forwardRef: remarkRef,
        validation: [{
            validate: validators.notEmpty(),
            failureMessage: 'Remark cannot be empty',
        }],
    };

    useAutofocus(remarkRef);

    return <Form 
        fields={[remarkField]}
        groups={[remarkGroup]}
        canceled={() => dispatch(actions.shouldShowWordEditModal(false))}
        confirmed={() => dispatch(actions.updateWordElement(wordEditPath, remarkRef.current.value))}
    />;
};

RemarkEditDialog.propTypes = {
    editting: PropTypes.bool,
};

export default RemarkEditDialog;
