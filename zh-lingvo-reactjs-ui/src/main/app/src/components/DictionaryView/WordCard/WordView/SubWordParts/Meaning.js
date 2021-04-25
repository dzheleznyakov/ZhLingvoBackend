import React, { Fragment } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './Meaning.module.scss';

import { Remark } from '.';
import Editing from './Editing';
import { REMARK_EDIT, REMARK_NEW, REMARK_DELETE } from '../../../../../static/constants/wordEditModalTypes';
import { IconButton, iconButtonTypes, buttonSizes } from '../../../../UI';
import Translation from './Translation';
import Example from './Example';
import { meaningType } from '../wordTypes';
import * as actions from '../../../../../store/actions';
import * as selectors from '../../../../../store/selectors';

const Meaning = props => {
    const { meaning, path } = props;
    const dispatch = useDispatch();
    const isEditing = useSelector(selectors.isEditingSelector);

    const remarkPath = [...path, 'remark'];
    const remark = meaning.remark ? (
        <Editing
            path={remarkPath}
            editModalType={REMARK_EDIT}
            deleteModalType={REMARK_DELETE}
        >
            <Remark value={meaning.remark} />
        </Editing>
    ) : isEditing && (
        <Fragment>
            <IconButton
                type={iconButtonTypes.NEW}
                size={buttonSizes.SMALL}
                clicked={() => {
                    dispatch(actions.shouldShowWordEditModal(true));
                    dispatch(actions.setWordEditModalType(REMARK_NEW, remarkPath));
                }}
            />
            {' '}
        </Fragment>
    );
    const translations = (meaning.translations || [])
        .map(tr => <Translation key={tr.id} entry={tr} />);
    const examples = (meaning.examples || [])
        .map(ex => <Example key={ex.id} entry={ex} />);

    return (
        <li className={classes.Meaning}>
            {remark}
            {translations}
            {examples}
        </li>
    );
};

Meaning.propTypes = {
    meaning: meaningType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
};

export default Meaning;
