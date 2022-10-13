import React, { useEffect } from 'react';
import { useParams } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';

import classes from './RecordList.module.scss';

import { useActionOnMount } from '../../../hooks';
import * as actions from '../../../store/actions';
import * as selectors from '../../../store/selectors';
import ListView from '../../Common/ListView/ListView';
import RecordListItem from './RecordListItem/RecordListItem';

const RecordList = () => {
    const { qid: quizId, rid } = useParams();
    const dispatch = useDispatch();
    const overviews = useSelector(selectors.quizRecordsOverviewsSelector);
    
    useActionOnMount(actions.fetchQuizRecords(quizId));

    useEffect(() => {
        if (rid !== null && rid !== undefined) {
            const selectedQuizRecordIndex = overviews.findIndex(qRec => qRec.id === +rid);
            dispatch(actions.selectQuizRecord(selectedQuizRecordIndex));
        }
    }, [overviews]);

    const wrapperClasses = [classes.QuizRecordListWrapper];
    if (rid !== null && rid !== undefined)
        wrapperClasses.push(classes.Active);

    const items = overviews.map(overview => 
        ({ key: `${overview.id}`, node: <RecordListItem quizRecord={overview} /> }));
    const onRecordClick = index => () => {
        dispatch(actions.selectQuizRecord(index));
        const recordId = overviews[index].id;
        dispatch(actions.navigateTo(`/tutor/quiz/${quizId}/${recordId}`));
    };
    const defaultSelectedIndex = () => {
        if (rid !== null && rid !== undefined)
            return overviews.findIndex(qRec => qRec.id === +rid);
        return -1;
    };
    const listView = items.length === 0
        ? null
        : <ListView 
            items={items}
            onItemClick={onRecordClick} 
            defaultSlectedIndex={defaultSelectedIndex}
            width={200} />;
    
    return (
        <div className={wrapperClasses.join(' ')}>
            {listView}
        </div>
    );
};

export default RecordList;
