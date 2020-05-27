/**
 * Licensed to EsupPortail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * EsupPortail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.papercut.dao;

import org.esupportail.papercut.domain.PayPapercutTransactionLog;
import org.esupportail.papercut.domain.PayPapercutTransactionLog.PayMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PayPapercutTransactionLogRepository extends JpaRepository<PayPapercutTransactionLog, Long>{

	Long countByUidAndArchived(String uid, Boolean archived);

	List<PayPapercutTransactionLog> findAllByTransactionDateBeforeAndUidIsNotLike(Date oldDate, String uid);

	Page<PayPapercutTransactionLog> findPayPapercutTransactionLogsByIdtransAndPayMode(String idTrans, PayMode payMode, Pageable pageable);

	Page<PayPapercutTransactionLog> findPayPapercutTransactionLogsByUid(String uid, Pageable pageable);

	Page<PayPapercutTransactionLog> findPayPapercutTransactionLogsByUidAndArchived(String uid, Boolean archived, Pageable pageable);

}