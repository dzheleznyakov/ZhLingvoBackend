import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './Meaning.module.scss';

import { EditableRemark } from '.';
import { EditableTranslation, NULL_TRANSLATION } from '.';
import Example from './Example';
import { meaningType } from '../wordTypes';
import * as selectors from '../../../../../store/selectors';

const TRANSLATIONS_PATH_SEGMENT = 'translations';

const getTranslations = (meaning, path, isEditing) => {
    const translations = (meaning.translations || [])
        .sort((a, b) => {
            if (a === null)
                return 1;
            if (b === null)
                return -1;
            return a.id < b.id ? -1 : 1;
        })
        .map((tr, i) => (tr === null ? null :
            <EditableTranslation 
                key={tr.id} 
                path={[...path, TRANSLATIONS_PATH_SEGMENT, `${i}`]} 
                entry={tr}
                postfix={i < meaning.translations.length - 1 ? '; ': ' '}
            />
        ));
    if (isEditing)
        translations.push(<EditableTranslation
            key="translation-new"
            path={[...path, TRANSLATIONS_PATH_SEGMENT, `${meaning.translations.length}`]}
            entry={NULL_TRANSLATION}
        />);
    return translations;
};

const Meaning = props => {
    const { meaning, path } = props;
    const dispatch = useDispatch();
    const isEditing = useSelector(selectors.isEditingSelector);

    const remarkPath = [...path, 'remark'];
    const remark = <EditableRemark value={meaning.remark} path={remarkPath} />;
    const translations = getTranslations(meaning, path, isEditing);
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
