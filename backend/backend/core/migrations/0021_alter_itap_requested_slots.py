# Generated by Django 3.2.4 on 2021-07-18 10:54

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('core', '0020_alter_itap_requested_slots'),
    ]

    operations = [
        migrations.AlterField(
            model_name='itap',
            name='requested_slots',
            field=models.IntegerField(blank=True, default=0, null=True, unique=True),
        ),
    ]
