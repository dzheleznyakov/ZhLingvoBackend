import React from 'react';
import PropTypes from 'prop-types';

import classes from './NewPartButton.module.scss';

import * as allModalTypes from '../../../../../../static/constants/quizRecordEditModalTypes';
import OnHoverEditable from '../OnHoverEditable';

const NewPartButton = props => {
    const { block, modalType, path, label } = props;

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
    block: false,
};

export default NewPartButton;
