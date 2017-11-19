'use strict';

Holder.run({renderer:'canvas'});
$('.modal').one('shown.bs.modal', function (e) {Holder.run({renderer:'canvas'})});