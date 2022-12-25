import React, { useCallback, useMemo, useState } from 'react';
import PropTypes from 'prop-types';
import { useDispatch } from 'react-redux';

import classes from './ControlBox.module.scss';

import { IconButton, iconButtonTypes } from '../../UI';
import * as actions from '../../../store/actions';
import { useModal } from '../../../hooks';

export const MODAL_TYPES = {
    NEW: 'NEW',
    DELETE: 'DELETE',
    SETTINGS: 'SETTINGS',
    EDIT: 'EDIT',
    FORWARD: 'FORWARD',
    NONE: 'NONE',
};

const modalTypesArray = Object.keys(MODAL_TYPES).map(key => MODAL_TYPES[key]);

const ControlBox = props => {
    const { panelKeyPrefix, disabled, panels } = props;
    const [modalType, setModalType] = useState(MODAL_TYPES.NONE);
    const showModal = modalType !== MODAL_TYPES.NONE;

    const closeModal = () => setModalType(MODAL_TYPES.NONE);

    const panelContainer = panels
        .filter(panelConfig => panelConfig.modalType === modalType)
        .map(panelConfig => panelConfig.panel);
    const Panel = panelContainer.length === 0 ? () => null : panelContainer[0];
    const panel =  <Panel close={closeModal} />;

    const buttons = panels
        .map(panelConfig => {
            const buttonType = panelConfig.modalType;

            let { disabled: buttonDisabled } = panelConfig;
            if (buttonDisabled == null)
                buttonDisabled = disabled;

            let buttonClicked = panelConfig.clicked;
            if (buttonClicked == null)
                buttonClicked = () => setModalType(buttonType);

            return <IconButton
                key={panelKeyPrefix + buttonType}
                type={iconButtonTypes[buttonType]}
                clicked={buttonClicked}
                disabled={buttonDisabled} 
            />;
        });

    useModal(showModal, closeModal, panel, [modalType]);

    return (
        <div className={classes.ButtonBox}>
                {buttons}
        </div>
    );
};

ControlBox.propTypes = {
    panelKeyPrefix: PropTypes.string.isRequired,
    disabled: PropTypes.bool,
    panels: PropTypes.arrayOf(
            PropTypes.shape({
                modalType: PropTypes.oneOf(modalTypesArray).isRequired,
                panel: PropTypes.func,
                clicked: PropTypes.func,
                disabled: PropTypes.bool,
        })
    ),
};

ControlBox.defaultProps = {
    disabled: true,
    panels: [],
};

export default ControlBox;
