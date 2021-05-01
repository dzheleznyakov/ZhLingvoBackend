import React from 'react';
import { useDispatch } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './NewPartButton.module.scss';

import { IconButton, iconButtonTypes, buttonSizes } from '../../../../UI';
import OnHoverEditable from './Editable/OnHoverEditable';
import * as actions from '../../../../../store/actions';
import * as allModalTypes from '../../../../../static/constants/wordEditModalTypes';

const NewPartButton = props => {
    const { block, modalType, path, label } = props;
    const dispatch = useDispatch();

    return (
        <OnHoverEditable
            newModalType={modalType}
            path={path}
            block={block}
        >
            <span className={classes.Label}>
                {`<${label}>`}
            </span>
        </OnHoverEditable>
    );
};

const allModalTypesArr = Object.keys(allModalTypes)
    .map(key => allModalTypes[key])
    .filter(type => type && type.indexOf('NEW') >= 0);

NewPartButton.propTypes = {
    label: PropTypes.string.isRequired,
    block: PropTypes.bool,
    modalType: PropTypes.oneOf(allModalTypesArr),
    path: PropTypes.arrayOf(PropTypes.string),
};

NewPartButton.defaultProps = {
    path: [],
};

export default NewPartButton;
